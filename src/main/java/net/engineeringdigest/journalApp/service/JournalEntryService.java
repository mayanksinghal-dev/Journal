package net.engineeringdigest.journalApp.service;

import java.util.List;
import java.util.Optional;

import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.entity.User;

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
            throw new RuntimeException(runExcp.getMessage(), runExcp.getCause());
        }
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public void remove(ObjectId id, String userName) {
        try {
            Optional<User> user = userService.findByUserName(userName);
            user.ifPresentOrElse(
                    userDetails -> {
                        boolean genuineJournal = userDetails.getJournalEntries().removeIf(journal -> journal.getId().equals(id));
                        if(genuineJournal){
                            userService.createUser(userDetails);
                            journalEntryRepository.deleteById(id);
                        }
                        else{
                            throw new RuntimeException("You are not right owner of the entry");
                        }
                    }, () -> {
                        throw new UsernameNotFoundException("Invalid user credentials");
                    });
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }
}