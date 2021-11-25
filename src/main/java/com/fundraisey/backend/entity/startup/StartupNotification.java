package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "startup_notification")
public class StartupNotification extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(targetEntity = Startup.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    private Startup startup;

    @Column(name = "message")
    private String message;

    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne(targetEntity = StartupNotificationType.class)
    @JoinColumn(name = "id_startup_notification_type", referencedColumnName = "id")
    private StartupNotificationType startupNotificationType;
}
