# 0002. Transactional outbox instead of 2-phase commit

## Context

Placing an order must both persist the order in Postgres and notify other systems (RabbitMQ
message, and later the AWS S3 export / SQS audit side-channels). A naive implementation would
either:

- perform a dual write (save to DB, then publish to the broker) - if the process crashes or the
  broker is unreachable between the two steps, the order exists but downstream systems never learn
  about it (or vice versa if ordered the other way round), or
- use an XA/2-phase-commit transaction spanning the database and the broker - technically solves
  atomicity, but couples the two systems' availability together (broker downtime blocks order
  placement), adds significant operational complexity, and is not supported by all target brokers
  (nor by AWS SQS/S3 at all).

## Decision

Use the [transactional outbox pattern](https://microservices.io/patterns/data/transactional-outbox.html):

- `SaveOrderAdapter` writes the order row and a `PENDING` row in `OUTBOX_EVENT` in the *same*
  local database transaction - this is a single-resource commit, so it is atomic without XA.
- `OutboxEventPublisher` polls pending outbox rows on a schedule, reloads the full aggregate, and
  publishes it through the existing RabbitMQ adapter. Only once publishing succeeds is the outbox
  row marked `SENT`.
- The AWS S3/SQS side-channels (added later) hang off the same publisher as **best-effort**
  operations: their failure never blocks the outbox row from being marked `SENT`, since they are
  supplementary (export/audit), not core to order processing.

## Consequences

- Order placement never fails or blocks because of broker/AWS unavailability - it only depends on
  the local database transaction succeeding.
- Message delivery is at-least-once and eventually consistent (there is a poll interval,
  `outbox.publisher.poll-interval-ms`, default 5s) rather than immediate/synchronous.
- Consumers of the RabbitMQ message must be idempotent (already true of the existing message
  design), since retries after a publish failure could in principle redeliver.
- Adds an extra table (`OUTBOX_EVENT`) and a background poller, which is more moving parts than a
  direct publish call - an accepted trade-off for correctness under partial failure.
