package archunit.suite;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tngtech.archunit.core.importer.ClassFileImporter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import archunit.ArchitectureElement;
import archunit.HexagonalArchitecture;

import static org.assertj.core.api.Assertions.assertThat;

import static archunit.ArchitectureElement.APPLICATION_PACKAGE_IDENTIFIER;
import static archunit.ArchitectureElement.MAIN_PACKAGE_IDENTIFIER;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Class representing adapters of hexagonal architecture pattern.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HexagonalArchitecturePatternTest {

    private static final Path DOMAIN_PATH = Paths.get("src", "main", "java", "com", "cp", "ecommerce", "domain");

    private static final Map<String, List<String>> ALL_BOUNDED_CONTEXTS = new HashMap<>();

    @BeforeAll
    public static void setUp() {

        final String domainPath = DOMAIN_PATH.toFile().getAbsolutePath();
        final File[] domains = new File(domainPath).listFiles(File::isDirectory);
        if (domains != null) {

            Arrays.stream(domains).forEach(domain -> {

                ALL_BOUNDED_CONTEXTS.put(
                        domain.getName(),
                        Arrays.stream(Objects.requireNonNull(new File(domain.getAbsolutePath()).listFiles(File::isDirectory)))
                                .map(File::getName)
                                .collect(Collectors.toList()));
            });
        }
    }

    static Stream<Arguments> allBoundedContexts() {

        final List<Arguments> arguments = new ArrayList<>();
        ALL_BOUNDED_CONTEXTS.forEach((domain, subdomains) -> {

            arguments.add(Arguments.of(domain, subdomains));
        });
        return Stream.of(arguments.toArray(new Arguments[0]));
    }

    @Order(1)
    @Test
    void domainsAndSubdomainsShouldNotBeEmpty() {

        assertThat(ALL_BOUNDED_CONTEXTS).isNotEmpty();
        assertThat(ALL_BOUNDED_CONTEXTS.keySet()).isNotEmpty();
        assertThat(ALL_BOUNDED_CONTEXTS.values()).isNotEmpty();
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource("allBoundedContexts")
    void shouldHaveValidDomainObjectsWithAdapters(final String domain) {

        assertThat(domain).isNotEmpty();

        HexagonalArchitecture.basePackage()

                .withDomain(domain)
                .usecases()
                .incomingPorts()
                .outgoingPorts()
                .and()

                .withAdapters()
                .domain(domain)
                .and()

                .check(new ClassFileImporter().importPackages(MAIN_PACKAGE_IDENTIFIER));
    }

    @Order(3)
    @Test
    void shouldHaveProperPackageDependencies() {

        noClasses().that()
                .resideInAPackage(ArchitectureElement.DOMAIN_PACKAGE_IDENTIFIER)
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(APPLICATION_PACKAGE_IDENTIFIER)
                .check(new ClassFileImporter().importPackages(MAIN_PACKAGE_IDENTIFIER));
    }

}
