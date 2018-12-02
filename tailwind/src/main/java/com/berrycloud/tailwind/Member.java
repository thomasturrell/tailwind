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
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 255)
  private String firstName;

  @NotBlank
  @Size(max = 255)
  private String lastName;

  /**
   * The plans associated with this member.
   */
  @ManyToMany
  @JoinTable(name = "member_plan", inverseJoinColumns = { @JoinColumn(name = "plan_id") })
  private Set<Plan> plans = new HashSet<>();

}
