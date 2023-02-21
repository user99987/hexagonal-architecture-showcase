package com.cp.ecommerce.adapter.mail.freemarker;

import java.util.Optional;

import com.cp.ecommerce.adapter.mail.pdf.i18n.TranslatableString;
import com.cp.ecommerce.adapter.mail.pdf.i18n.TranslatableStringConverter;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Object wrapper for FreeMarker templates' processing.
 */
public class FreeMarkerCustomObjectWrapper extends DefaultObjectWrapper {

    private final transient TranslatableStringConverter translatableStringConverter;

    @SuppressWarnings("deprecation")
    public FreeMarkerCustomObjectWrapper(final TranslatableStringConverter translatableStringConverter) {

        this.translatableStringConverter = translatableStringConverter;
    }

    @Override
    public TemplateModel handleUnknownType(final Object obj) throws TemplateModelException {

        if (obj instanceof Optional) {
            final Optional<?> optional = (Optional<?>) obj;
            return wrap(optional.orElse(null));
        } else if (obj instanceof TranslatableString) {
            return wrap(translatableStringConverter.convert((TranslatableString) obj));
        } else {
            return super.handleUnknownType(obj);
        }
    }

}
