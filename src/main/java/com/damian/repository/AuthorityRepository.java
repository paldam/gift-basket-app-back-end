package com.damian.repository;

import com.damian.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Damian on 25.10.2017.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
