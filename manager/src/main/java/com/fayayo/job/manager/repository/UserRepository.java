package com.fayayo.job.manager.repository;

import com.fayayo.job.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dalizu on 2018/9/7.
 * @version v1.0
 * @desc
 */
public interface UserRepository extends JpaRepository<User,Integer>,JpaSpecificationExecutor {

     User findByUsernameAndPassword(String username,String password);

}
