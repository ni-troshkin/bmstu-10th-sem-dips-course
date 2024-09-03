import React, { useContext, useState } from 'react';
import DatePicker from 'react-datepicker';
import "react-datepicker/src/stylesheets/datepicker.scss";
import { InputProps as IProps } from "@chakra-ui/react";

import en from 'date-fns/locale/en-GB';
import styles from './DateInputBox.module.scss';
import { DateContext } from 'components/DateInput/DateInput';


interface DateProps extends IProps {
    setDate?: CallableFunction,
}

const DateInputBox: React.FC<DateProps> = (props) => {
    const [tillDate, setTillDate] = useState('');
    
    const { setDate } = useContext(DateContext)!;

    return (
        <>
        <DatePicker className={styles.likes_box}
            showIcon
            selected={tillDate}
            onChange={(date) => {setDate(date); setTillDate(date)}}
            locale={en}
            dateFormat="dd.MM.yyyy"
        />
        </>
    );
}

export default DateInputBox;