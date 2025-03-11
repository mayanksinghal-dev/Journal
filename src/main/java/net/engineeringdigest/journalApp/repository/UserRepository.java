package net.engineeringdigest.journalApp.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import net.engineeringdigest.journalApp.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User,ObjectId>{
    Optional<User> findByUserName(String userName);
//Optional<User> findByUserName(String userName);
    void deleteByUserName(String userName);
}
