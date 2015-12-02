package br.com.projetodw.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.projetodw.model.Usuario;
import br.com.projetodw.service.NegocioException;
import br.com.projetodw.util.jpa.Transactional;

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Usuario porId(Long id) {
		return this.manager.find(Usuario.class, id);
	}

	// public List<Usuario> vendedores() {
	// // TODO filtrar apenas vendedores (por um grupo específico)
	// return this.manager.createQuery("from Usuario",
	// Usuario.class).getResultList();
	// }

	public List<Usuario> getUsuarios() {
		return this.manager.createQuery("from Usuario", Usuario.class).getResultList();
	}

	public Usuario porEmail(String email) {
		Usuario usuario = null;

		try {
			usuario = this.manager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
					.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}

		return usuario;
	}

	public Usuario guardar(Usuario usuario) {
		return manager.merge(usuario);
	}

	@Transactional
	public void remover(Usuario usuario) {
		try {
			usuario = porId(usuario.getId());
			manager.remove(usuario);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Usuário não pode ser excluído.");
		}
	}

}