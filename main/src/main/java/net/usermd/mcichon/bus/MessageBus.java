package net.usermd.mcichon.bus;

public interface MessageBus {
    MessageBuilder message(String subject);
    ResponseBuilder emit(String subject);
    MessageBus initThreads(int threads) throws IllegalArgumentException;
}
