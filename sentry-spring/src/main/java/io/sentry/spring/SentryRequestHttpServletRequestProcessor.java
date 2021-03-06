package io.sentry.spring;

import com.jakewharton.nopen.annotation.Open;
import io.sentry.EventProcessor;
import io.sentry.SentryEvent;
import io.sentry.spring.tracing.TransactionNameProvider;
import io.sentry.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Attaches information about HTTP request to {@link SentryEvent}. */
@Open
public class SentryRequestHttpServletRequestProcessor implements EventProcessor {
  private final @NotNull HttpServletRequest request;
  private final @NotNull SentryRequestResolver sentryRequestResolver;
  private final @NotNull TransactionNameProvider transactionNameProvider =
      new TransactionNameProvider();

  public SentryRequestHttpServletRequestProcessor(
      final @NotNull HttpServletRequest request,
      final @NotNull SentryRequestResolver sentryRequestResolver) {
    this.request = Objects.requireNonNull(request, "request is required");
    this.sentryRequestResolver =
        Objects.requireNonNull(sentryRequestResolver, "sentryRequestResolver are required");
  }

  @Override
  public @NotNull SentryEvent process(
      final @NotNull SentryEvent event, final @Nullable Object hint) {
    event.setRequest(sentryRequestResolver.resolveSentryRequest(request));
    if (event.getTransaction() == null) {
      event.setTransaction(transactionNameProvider.provideTransactionName(request));
    }
    return event;
  }
}
