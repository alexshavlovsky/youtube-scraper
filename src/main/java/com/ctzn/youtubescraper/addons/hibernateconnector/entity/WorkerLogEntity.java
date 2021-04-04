package com.ctzn.youtubescraper.addons.hibernateconnector.entity;

import com.ctzn.youtubescraper.core.persistence.dto.WorkerLogDTO;
import lombok.*;

import javax.persistence.*;

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

    @Embedded
    private ContextStatus contextStatus;

    public static WorkerLogEntity fromWorkerLogDTO(WorkerLogDTO dto) {
        return new WorkerLogEntity(
                null,
                dto.getContextId(),
                ContextStatus.fromContextStatusDTO(dto.getContextStatus())
        );
    }

}
