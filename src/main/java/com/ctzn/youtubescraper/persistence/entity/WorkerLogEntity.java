package com.ctzn.youtubescraper.persistence.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "worker_log")
public class WorkerLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    private String contextId;
    public Date startedDate;
    public Date finishedDate;
    private String status;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String message;
}
