package com.allforone.starvestop.domain.store.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

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

    @Column(nullable = false)
    private Point location;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StoreStatus status;

    private Store(
            User user,
            String storeName,
            String address,
            String description,
            StoreCategory category,
            Point location,
            LocalTime openTime,
            LocalTime closeTime
    ) {
        this.user = user;
        this.storeName = storeName;
        this.address = address;
        this.description = description;
        this.category = category;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = StoreStatus.OPENED;
    }

    public static Store create(
            User user,
            String storeName,
            String address,
            String description,
            StoreCategory category,
            Point location,
            LocalTime openTime,
            LocalTime closeTime

    ) {

        return new Store(
                user,
                storeName,
                address,
                description,
                category,
                location,
                openTime,
                closeTime
        );
    }
}
