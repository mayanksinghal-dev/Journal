package net.engineeringdigest.journalApp.user;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import net.engineeringdigest.journalApp.user.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User,ObjectId>{
    Optional<User> findByUserName(String userName);
}
