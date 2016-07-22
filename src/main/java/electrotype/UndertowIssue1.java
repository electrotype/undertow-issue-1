package electrotype;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;

public class UndertowIssue1 {

    public static void main(String[] args) {
        UndertowIssue1 app = new UndertowIssue1();
        app.start();
    }

    private Undertow server;

    protected void start() {

        Builder builder = Undertow.builder().setHandler(getRootHandler());
        builder.addHttpListener(44444, "localhost");
        this.server = builder.build();

        this.server.start();
        System.out.println("Call : http://localhost:44444/aaa/bbb");
    }

    protected HttpHandler getRootHandler() {

        PathHandler handler1PathHandler = new PathHandler(new DefaultHandler());
        handler1PathHandler.addExactPath("/aaa/bbb", new Handler1());

        return handler1PathHandler;
    }

    /**
     * Default Handler
     */
    protected static class DefaultHandler implements HttpHandler {

        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            System.out.println("Default Handler");
        }
    }

    /**
     * Handler #1
     */
    protected static class Handler1 implements HttpHandler {

        private PathHandler handler2PathHandler;

        protected PathHandler getHandler2PathHandler() {
            if(this.handler2PathHandler == null) {
                this.handler2PathHandler = new PathHandler(new DefaultHandler());
                this.handler2PathHandler.addExactPath("/aaa/bbb", new Handler2());
            }
            return this.handler2PathHandler;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            System.out.println("Handler1");
            getHandler2PathHandler().handleRequest(exchange);
        }
    }

    /**
     * Handler #2
     */
    protected static class Handler2 implements HttpHandler {

        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            System.out.println("Handler2");
        }
    }

}
