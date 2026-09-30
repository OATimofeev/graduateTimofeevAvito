package ru.skypro.homework.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.db.model.AdModel;
import ru.skypro.homework.db.model.CommentModel;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel, Integer> {
    List<CommentModel> findAllByAd(AdModel ad);
}
