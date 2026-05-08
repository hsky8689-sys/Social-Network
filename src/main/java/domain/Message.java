package domain;

import java.util.Date;
import java.util.Objects;

public class Message {
    private Long id_mesaj;
    Long sender,reciever;
    private String content;
    private Date timestamp;
    public Message(Long id, Long sender, Long reciever, String content, Date timestamp) {
        this.id_mesaj = id;
        this.sender = sender;
        this.reciever = reciever;
        this.content = content;
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return  id_mesaj + "|" +
                sender + "|" +
                reciever + "|"+
                content + "|" +
                timestamp + "\n"
                ;
    }
    public Long getId() {
        return id_mesaj;
    }
    public Long getSender() {
        return sender;
    }
    public Long getReciever() {
        return reciever;
    }
    public String getContent() {
        return content;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return timestamp.equals(message.timestamp) && Objects.equals(id_mesaj, message.id_mesaj) && Objects.equals(sender, message.sender) && Objects.equals(reciever, message.reciever) && Objects.equals(content, message.content);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id_mesaj, sender, reciever, content, timestamp);
    }
}