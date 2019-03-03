package net.usermd.mcichon.BusTypes;

public class BusImpl {
    private BusType busType;

    public BusImpl(BusType busType) {
        this.busType = busType;
    }

    public Object process() {
        return this.busType.getDispatcher();
    }

    public enum BusTypeEnum implements BusType {
        ASYNC {
            @Override
            public BusTypeEnum.Async getDispatcher() {
                return new BusTypeEnum.Async();
            }
        },
        SYNC {
            @Override
            public BusTypeEnum.Sync getDispatcher() {
                return new BusTypeEnum.Sync();
            }
        };

        private static class Async implements Bus {

            private final String TYPE = "Async operation";

            private Async() {
            }

            @Override
            public Async process(Object event) {
                return this;
            }

            @Override
            public String toString() {
                return "Async{" +
                        "TYPE='" + TYPE + '\'' +
                        '}';
            }
        }

        private static class Sync implements Bus {

            private final String TYPE = "Sync operation";

            private Sync() {
            }

            @Override
            public Sync process(Object event) {
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
