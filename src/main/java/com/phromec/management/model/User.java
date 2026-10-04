package com.phromec.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements Serializable{

	@Serial
    private static final long serialVersionUID = 5926468583005150707L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

    @Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

    @Column(name = "full_name", nullable = false, length = 100)
	private String fullName;

    @Column(name = "email", unique = true, length = 150)
	private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

    @Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "is_active")
	private Boolean isActive;


    @Column(name = "created_at")
	private LocalDateTime createdAt;

	// Default Constructor
	public User() {
	}

    public Boolean isActive() {
		return isActive;
	}

    @Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", fullName='" + fullName + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", isActive=" + isActive +
				", createdAt=" + createdAt +
				'}';
	}
}
