/**
 * 
 */
package jp.sample.holiday.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.sample.holiday.domain.model.HolidayDateModel;

/**
 * @author nbkzk
 *
 */
public interface HolidayDateModelRepository extends JpaRepository<HolidayDateModel, Long> {

    @Query(value = "select  t.* "
                    + "from  holiday_date_model as t "
                    + "where t.create_at = (select max(internal.create_at) from holiday_date_model as internal) ",
            nativeQuery = true)
    List<HolidayDateModel> maxCreateAt();

    @Query(value = "select  t.* "
                    + "from  holiday_date_model as t "
                    + "where t.date between :startDate and :endDate ",
            nativeQuery = true)
    List<HolidayDateModel> findByYear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    public Optional<HolidayDateModel> findByDate(LocalDate date);

}
