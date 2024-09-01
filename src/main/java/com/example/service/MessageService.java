package com.example.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message msg) {
        String msgText = msg.getMessageText();
        if(msgText.length() == 0 || msgText.length() >= 255 || !accountRepository.existsById(msg.getPostedBy())){
            return null;
        }
        return messageRepository.save(msg);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> msg = messageRepository.findById(messageId);
        if(msg.isPresent()){
            return msg.get();
        } else {
            return null;
        }
    }

    public int deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)){ 
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    public int updateMessage(Integer messageId, String messageText) {
        if(messageText.length() > 255 || messageText.length() == 0 || !messageRepository.existsById(messageId)){
            return 0;
        }
        Optional<Message> msg = messageRepository.findById(messageId);
        msg.get().setMessageText(messageText);
        messageRepository.save(msg.get());
        return 1;
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }
}
