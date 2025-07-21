package com.bipsqwake.anxios_shop_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bipsqwake.anxios_shop_api.entity.Item;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Transactional
public interface ItemRepository extends JpaRepository<Item, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Item findByIdForUpdate(@Param("id") String id);

    List<Item> findByItemsLeftGreaterThanOrderByIntName(int value);
    Optional<Item> findByIntName(String intName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.intName = :intName")
    Optional<Item> findByIntNameForUpdate(String intName);

    @Query("SELECT i.intName FROM Item i WHERE i.itemsLeft = :itemsLeft ORDER BY i.intName")
    List<String> findIntNameByItemsLeft(int itemsLeft);

    void deleteByIntName(String intName);

    Page<Item> findAll(Pageable pageable);
}
