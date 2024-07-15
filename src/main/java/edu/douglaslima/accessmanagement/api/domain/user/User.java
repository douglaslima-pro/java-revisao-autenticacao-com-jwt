package edu.douglaslima.accessmanagement.api.domain.user;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private String Id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String password;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tb_user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private List<String> roles;
	
}
