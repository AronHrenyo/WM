package com.wm.service.pdf;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;
import java.io.ByteArrayOutputStream;

public abstract class AbstractPdfService<T> {

    protected final TemplateEngine templateEngine;

    protected AbstractPdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    protected byte[] generate(String template, String variableName, T data) throws Exception {

        Context context = new Context();
        context.setVariable(variableName, data);

        String html = templateEngine.process(template, context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }
}