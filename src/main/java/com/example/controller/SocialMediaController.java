package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
@RestController
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /*
     * 
     * ## 1: Our API should be able to process new User registrations.
     * 
     * As a user, I should be able to create a new Account on the endpoint POST
     * localhost:8080/register. The body will contain a representation of a JSON
     * Account, but will not contain an accountId.
     * 
     * - The registration will be successful if and only if the username is not
     * blank, the password is at least 4 characters long, and an Account with that
     * username does not already exist. If all these conditions are met, the
     * response body should contain a JSON of the Account, including its accountId.
     * The response status should be 200 OK, which is the default. The new account
     * should be persisted to the database.
     * - If the registration is not successful due to a duplicate username, the
     * response status should be 409. (Conflict)
     * - If the registration is not successful for some other reason, the response
     * status should be 400. (Client error)
     * 
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account acc) {
        Account newAccount = accountService.register(acc);
        if (newAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(newAccount);
    }

    /*
     * ## 2: Our API should be able to process User logins.
     * 
     * As a user, I should be able to verify my login on the endpoint POST
     * localhost:8080/login. The request body will contain a JSON representation of
     * an Account.
     * 
     * - The login will be successful if and only if the username and password
     * provided in the request body JSON match a real account existing on the
     * database. If successful, the response body should contain a JSON of the
     * account in the response body, including its accountId. The response status
     * should be 200 OK, which is the default.
     * - If the login is not successful, the response status should be 401.
     * (Unauthorized)
     */
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account acc) {
        Account newAccount = accountService.loginAccount(acc);
        if (newAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(newAccount);
    }

    /*
     * ## 3: Our API should be able to process the creation of new messages.
     * 
     * As a user, I should be able to submit a new post on the endpoint POST
     * localhost:8080/messages. The request body will contain a JSON representation
     * of a message, which should be persisted to the database, but will not contain
     * a messageId.
     * 
     * - The creation of the message will be successful if and only if the
     * messageText is not blank, is not over 255 characters, and postedBy refers to
     * a real, existing user. If successful, the response body should contain a JSON
     * of the message, including its messageId. The response status should be 200,
     * which is the default. The new message should be persisted to the database.
     * - If the creation of the message is not successful, the response status
     * should be 400. (Client error)
     */
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message msg) {
        Message newMessage = messageService.createMessage(msg);
        if (newMessage == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(newMessage);
    }

    /*
     * 
     * ## 4: Our API should be able to retrieve all messages.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET
     * localhost:8080/messages.
     * 
     * - The response body should contain a JSON representation of a list containing
     * all messages retrieved from the database. It is expected for the list to
     * simply be empty if there are no messages. The response status should always
     * be 200, which is the default.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    /*
     * ## 5: Our API should be able to retrieve a message by its ID.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET
     * localhost:8080/messages/{messageId}.
     * 
     * - The response body should contain a JSON representation of the message
     * identified by the messageId. It is expected for the response body to simply
     * be empty if there is no such message. The response status should always be
     * 200, which is the default.
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return ResponseEntity.ok(messageService.getMessageById(messageId));

    }

    /*
     * 
     * ## 6: Our API should be able to delete a message identified by a message ID.
     * 
     * As a User, I should be able to submit a DELETE request on the endpoint DELETE
     * localhost:8080/messages/{messageId}.
     * 
     * - The deletion of an existing message should remove an existing message from
     * the database. If the message existed, the response body should contain the
     * number of rows updated (1). The response status should be 200, which is the
     * default.
     * - If the message did not exist, the response status should be 200, but the
     * response body should be empty. This is because the DELETE verb is intended to
     * be idempotent, ie, multiple calls to the DELETE endpoint should respond with
     * the same type of response.
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<String> deleteMessageById(@PathVariable Integer messageId) {
        int numRows = messageService.deleteMessageById(messageId);
        if (numRows == 1) {
            return ResponseEntity.ok("1");
        }
        return ResponseEntity.ok().build();
    }

    /*
     * ## 7: Our API should be able to update a message text identified by a message
     * ID.
     * 
     * As a user, I should be able to submit a PATCH request on the endpoint PATCH
     * localhost:8080/messages/{messageId}. The request body should contain a new
     * messageText values to replace the message identified by messageId. The
     * request body can not be guaranteed to contain any other information.
     * 
     * - The update of a message should be successful if and only if the message id
     * already exists and the new messageText is not blank and is not over 255
     * characters. If the update is successful, the response body should contain the
     * number of rows updated (1), and the response status should be 200, which is
     * the default. The message existing on the database should have the updated
     * messageText.
     * - If the update of the message is not successful for any reason, the response
     * status should be 400. (Client error)
     * 
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<String> updateMessage(@PathVariable Integer messageId, @RequestBody Message msg) {
        if (messageService.updateMessage(messageId, msg.getMessageText()) == 1) {
            return ResponseEntity.ok("1");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /*
     * ## 8: Our API should be able to retrieve all messages written by a particular
     * user.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET
     * localhost:8080/accounts/{accountId}/messages.
     * 
     * - The response body should contain a JSON representation of a list containing
     * all messages posted by a particular user, which is retrieved from the
     * database. It is expected for the list to simply be empty if there are no
     * messages. The response status should always be 200, which is the default.
     */
    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId){
        return ResponseEntity.ok(messageService.getMessagesByAccountId(accountId));
    }
}
