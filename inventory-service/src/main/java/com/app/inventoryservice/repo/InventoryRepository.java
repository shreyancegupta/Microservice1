package com.app.inventoryservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.inventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    Optional<Inventory> findBySkuCode(String skuCode);
}
