package net.usermd.mcichon.bus;

public interface MessageBus {
    MessageBuilder message(String subject);
    ResponseBuilder emit(String subject);
    public MessageBus initBus(int threads) throws IllegalArgumentException;
}
