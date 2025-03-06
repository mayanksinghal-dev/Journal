package net.engineeringdigest.journalApp.journalEntry;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.engineeringdigest.journalApp.journalEntry.entity.JournalEntry;
import net.engineeringdigest.journalApp.user.UserRepository;
import net.engineeringdigest.journalApp.user.UserService;
import net.engineeringdigest.journalApp.user.entity.User;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createEntry(JournalEntry journalEntry, String userName) {
        try {
            Optional<User> user = userService.findByUserName(userName);
            user.ifPresentOrElse(userDetails -> {
                JournalEntry saved = journalEntryRepository.save(journalEntry);
                userDetails.getJournalEntries().add(saved);
                userRepository.save(userDetails);
            }, () -> {
                throw new RuntimeException("Invalid user credentials");
            });
        } catch (RuntimeException runExcp) {
            runExcp.printStackTrace(); // Log the error properly
            throw new RuntimeException("Error creating journal entry", runExcp);
        }
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void remove(ObjectId id, String userName) {
        try {
            Optional<User> user = userService.findByUserName(userName);
            user.ifPresentOrElse(
                    userDetails -> {
                        user.get().getJournalEntries().removeIf(journal -> journal.getId().equals(id));
                        userService.createUser(user.get());
                        journalEntryRepository.deleteById(id);
                    }, () -> {
                        throw new RuntimeException("Invalid user credentials");
                    });
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid user credentials");
        }
    }
}