package org.choongang.admin.board.controller.config.repositories;

import org.choongang.admin.board.controller.config.entities.Configs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigsRepository extends JpaRepository<Configs, String > {



}
