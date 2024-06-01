package com.assignment.fluxkart.Repository;

import com.assignment.fluxkart.Models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("SELECT COUNT(c) FROM Contact c WHERE c.phoneNumber = :number")
    Long findCountByPhoneNumber(@Param("number") Integer number);

    @Query("SELECT COUNT(c) FROM Contact c WHERE c.email = :email")
    Long findCountByEmail(@Param("email") String email);

    @Query("SELECT COUNT(c) FROM Contact c WHERE c.email = :email OR c.phoneNumber = :number")
    Integer findCountByEmailOrPhoneNumber(@Param("email") String email, @Param("number") Integer number);

    @Query("SELECT COUNT(c) FROM Contact c WHERE c.linkedId = :id")
    Integer findCountByLinkedId(@Param("id") Integer id);

    @Query("SELECT DISTINCT c.phoneNumber FROM Contact c WHERE c.linkedId = :id OR c.id = :id")
    Set<Integer> findDistinctPhoneNumbersByLinkedIdOrId(@Param("id") Integer id);

    @Query("SELECT DISTINCT c.email FROM Contact c WHERE c.linkedId = :id OR c.id = :id")
    Set<String> findDistinctEmailsByLinkedIdOrId(@Param("id") Integer id);

    @Query("SELECT c.id FROM Contact c WHERE c.linkedId = :id")
    Set<Integer> findIdsByLinkedId(@Param("id") Integer id);

    @Query("SELECT CASE WHEN c.linkedId IS NOT NULL THEN MIN(c.linkedId) ELSE MIN(c.id) END " +
            "FROM Contact c WHERE c.email = :email OR c.phoneNumber = :number")
    Integer findIdByEmailAndPhoneNumber(@Param("email") String email, @Param("number") Integer number);


    @Query("SELECT MIN(c.linkedId) FROM Contact c WHERE c.email = :email")
    Integer findMinLinkedIdByEmail( String email);

    @Query("SELECT MIN(c.linkedId) FROM Contact c WHERE c.phoneNumber = :phoneNumber")
    Integer findMinLinkedIdByPhoneNumber(@Param("phoneNumber") Integer phoneNumber);

    @Query("SELECT COALESCE(\n" +
            "  (SELECT c1.linkedId \n" +
            "   FROM Contact c1 \n" +
            "   WHERE c1.email = :email \n" +
            "   ORDER BY c1.id ASC), \n" +
            "  (SELECT MIN(c2.id) \n" +
            "   FROM Contact c2 \n" +
            "   WHERE c2.email = :email)\n" +
            ") \n" +
            "FROM Contact c")
    Integer findPrimaryIdByEmailIfExistOrMinId(String email);

    @Query("SELECT COALESCE(\n" +
            "  (SELECT c1.linkedId \n" +
            "   FROM Contact c1 \n" +
            "   WHERE c1.phoneNumber = :number \n" +
            "   ORDER BY c1.id ASC), \n" +
            "  (SELECT MIN(c2.id) \n" +
            "   FROM Contact c2 \n" +
            "   WHERE c2.phoneNumber = :number)\n" +
            ") \n" +
            "FROM Contact c")
    Integer findPrimaryIdByPhNumberIfExistOrMinId(Integer number);

    @Modifying
    @Transactional
    @Query(
            "UPDATE Contact c " +
                    "SET c.updatedAt = CURRENT_TIMESTAMP, " +
                    "  c.linkedPrecedence = 'secondary', " +
                    "  c.linkedId = :newLinkedId " +
                    "WHERE c.linkedId = :oldLinkedId"
    )
    int updateContactDetailsByLinkedId(
            @Param("newLinkedId") Integer newLinkedId,
            @Param("oldLinkedId") Integer oldLinkedId
    );

    @Modifying
    @Transactional
    @Query(
            "UPDATE Contact c " +
                    "SET c.updatedAt = CURRENT_TIMESTAMP, " +
                    "  c.linkedPrecedence = 'secondary', " +
                    "  c.linkedId = :newLinkedId " +
                    "WHERE c.id = :oldLinkedId"
    )
    int updateContactDetailsById(
            @Param("newLinkedId") Integer newLinkedId,
            @Param("oldLinkedId") Integer oldLinkedId
    );


    Contact findByEmail(String email);

    Integer countByEmail(String email);

    Integer countByPhoneNumber(Integer number);

    Optional<Contact> findByEmailAndPhoneNumber(String email, Integer phoneNumber);
}
