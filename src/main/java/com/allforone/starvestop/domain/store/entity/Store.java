package com.allforone.starvestop.domain.store.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreCategory category;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreStatus status;

    @Column(nullable = false)
    private String businessRegistrationNumber;

    private Store(
            User user,
            String storeName,
            String address,
            String description,
            StoreCategory category,
            Point location,
            LocalTime openTime,
            LocalTime closeTime,
            StoreStatus status,
            String businessRegistrationNumber
    ) {
        this.user = user;
        this.storeName = storeName;
        this.address = address;
        this.description = description;
        this.category = category;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public static Store create(
            User user,
            String storeName,
            String address,
            String description,
            StoreCategory category,
            Point location,
            LocalTime openTime,
            LocalTime closeTime,
            StoreStatus status,
            String businessRegistrationNumber
    ) {

        return new Store(
                user,
                storeName,
                address,
                description,
                category,
                location,
                openTime,
                closeTime,
                status,
                businessRegistrationNumber
        );
    }

    public void update(
            String storeName,
            String address,
            String description,
            StoreCategory category,
            Point location,
            LocalTime openTime,
            LocalTime closeTime,
            StoreStatus status
    ) {
        this.storeName = storeName;
        this.address = address;
        this.description = description;
        this.category = category;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
    }
}
