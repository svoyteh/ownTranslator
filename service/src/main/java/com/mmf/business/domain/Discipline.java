package com.mmf.business.domain;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author svetlana.voyteh
 * @date: 5/11/12
 */
@XmlRootElement
public class Discipline implements DomainClass<Long>{

    private static final long serialVersionUID = -6361800191146842503L;
    private Long id;
    private String name;
    private DisciplineType disciplineType;
    private Long disciplineTypeId;

    public Discipline(){}

    public Discipline(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public DisciplineType getDisciplineType() {
        return disciplineType;
    }

    public void setDisciplineType(DisciplineType disciplineType) {
        this.disciplineType = disciplineType;
    }

    public Long getDisciplineTypeId() {
        return disciplineTypeId;
    }

    public void setDisciplineTypeId(Long disciplineTypeId) {
        this.disciplineTypeId = disciplineTypeId;
    }
}
