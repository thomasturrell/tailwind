/*
 * Copyright 2018 Berry Cloud s.r.o. All rights reserved.
 */

package com.berrycloud.tailwind;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Plan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * <p>
   * The name of this plan.
   * </p>
   */
  @NotBlank
  @Size(max = 255)
  private String name;

  /**
   * <p>
   * The organization that provides this plan.
   * </p>
   */
  @NotBlank
  @Size(max = 255)
  private String organization;

  /**
   * <p>
   * The members associated with this plan.
   * </p>
   */
  @ManyToMany
  @JoinTable(name = "member_plan", inverseJoinColumns = { @JoinColumn(name = "member_id") })
  private Set<Member> members = new HashSet<>();

}
