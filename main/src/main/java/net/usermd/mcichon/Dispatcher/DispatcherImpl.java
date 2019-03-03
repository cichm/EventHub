package net.usermd.mcichon.Dispatcher;

public class DispatcherImpl {
    private DispatcherType dispatcherType;

    public DispatcherImpl(DispatcherType dispatcherType) {
        this.dispatcherType = dispatcherType;
    }

    public Object process() {
        return this.dispatcherType.getDispatcher();
    }

    public enum DispatcherTypeEnum implements DispatcherType {
        ASYNC {
            @Override
            public Async getDispatcher() {
                return new Async();
            }
        },
        SYNC {
            @Override
            public Sync getDispatcher() {
                return new Sync();
            }
        };

        private static class Async implements Dispatcher {

            private final String TYPE = "Async operation";

            private Async() {
            }

            @Override
            public Async dispatch(Object event) {
                return this;
            }

            @Override
            public String toString() {
                return "Async{" +
                        "TYPE='" + TYPE + '\'' +
                        '}';
            }
        }

        private static class Sync implements Dispatcher {

            private final String TYPE = "Sync operation";

            private Sync() {
            }

            @Override
            public Sync dispatch(Object event) {
                return this;
            }

            @Override
            public String toString() {
                return "Sync{" +
                        "TYPE='" + TYPE + '\'' +
                        '}';
            }
        }
    }
}
