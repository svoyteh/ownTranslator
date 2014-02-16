package com.mmf.business;

import com.mmf.business.domain.Schedule;

import java.util.List;

/**
 * User: svetlana.voyteh
 * Date: 19.03.13
 */
public interface ScheduleService extends CrudService<Long, Schedule> {
    List<Schedule> getSchedule(int semester, int yearOfEntrance, String groupName, String subGroupName) throws BusinessServiceException;

    List<Schedule> getSchedule(long lecturerId, int semester) throws BusinessServiceException;
}
