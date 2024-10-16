package com.neuron.cv.dbrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.neuron.cv.dbentity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
