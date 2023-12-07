#!/bin/bash

echo "weekly Start" 

SQL="select trim(concat(replace(SUBSTRING_INDEX(SUBSTRING_INDEX(time_spec, 'time_spec-', -1),';',1),'.',':'),':00')) as t_time,trim(SUBSTRING_INDEX(SUBSTRING_INDEX(time_spec, 'time_spec-', -1),';weekdays-',-1)),s_id,sender,msisdn_lst,trim(msg_content) 
from msgweb.sms_req 
where 1=1
and status ='I'
and time_spec like '%selectedRoutineOption-weekly%' 
and concat(s_id,'#',sender,'#',DATE_FORMAT(now(),'%Y-%m-%d'),' ',replace(SUBSTRING_INDEX(SUBSTRING_INDEX(time_spec, 'time_spec-', -1),';',1),'.',':'),':00') not in (select concat(sms_box_seq,'#',a_num,'#',target_time) from msgweb.sms_send_msg)	
and  INSTR( time_spec, trim(DATE_FORMAT(now(),'%a')) )  > 0
"

echo -e $SQL 
