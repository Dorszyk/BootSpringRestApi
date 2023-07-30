package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

@Data
@With
@Entity
@Builder
@ToString(of = {"name", "status"})
@EqualsAndHashCode(of = "petId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "pet_store_pet_id", nullable = false)
    private Long petStorePetId;

    @Column(name = "name", nullable = false)

    private String name;
    @Column(name = "status", nullable = false)

    private String status;
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = false)
    private EmployeeEntity employee;

}
