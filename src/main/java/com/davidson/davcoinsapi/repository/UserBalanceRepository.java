package com.davidson.davcoinsapi.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

import com.davidson.davcoinsapi.model.UserBalance;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserBalanceRepository extends CrudRepository<UserBalance, UUID> {

}
