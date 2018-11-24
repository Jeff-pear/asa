package com.pears.asa.util.model;


import lombok.*;

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MailVO {
    private String fromUser;
    private String toUser;
    private String subject;
    private String content;

}
