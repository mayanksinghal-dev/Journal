package net.engineeringdigest.journalApp.controller;

import java.time.LocalDateTime;
import java.util.*;

import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.entity.User;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("journal")
@Slf4j
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalOfUser() {
        log.info("Get all journal of a user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> user = userService.findByUserName(userName);
        return user.map(userDetails -> {
                    List<JournalEntry> journalEntry = userDetails.getJournalEntries();
                    return journalEntry.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                            : new ResponseEntity<>(journalEntry, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.createEntry(myEntry, userName);
            return new ResponseEntity<JournalEntry>(myEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> findById(@PathVariable ObjectId id) {
        log.info("Get journal of a user by id");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Optional<User> validUser = userService.findByUserName(userName);
            return validUser.map(user -> {
                        JournalEntry journalEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
                        System.out.println("journalEntries" + journalEntries);
                        if (journalEntries != null) {
                            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
                            if (journalEntry.isPresent()) {
                                return new ResponseEntity<JournalEntry>(journalEntry.get(), HttpStatus.OK);
                            }
                        }
                        throw new RuntimeException("You are not right owner of the entry");
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("You ain't even a user, get lost MF"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> remove(@PathVariable ObjectId id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.remove(id, userName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            Optional<User> validUser = userService.findByUserName(userName);
            return validUser.map(user -> {
                        boolean validEntry = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).findFirst().isPresent();
                        if (validEntry) {
                            JournalEntry old = journalEntryService.findById(id).get();
                            if (old != null) {
                                old.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
                                old.setContent(newEntry.getContent());
                                journalEntryService.createEntry(old, userName);
                                return new ResponseEntity<>(old, HttpStatus.OK);
                            }
                        }
                        throw new RuntimeException("You are not right owner of the entry");
                    })
                    .orElseThrow(() ->
                            new UsernameNotFoundException("You ain't even a user, get lost MF")
                    );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

    }
}