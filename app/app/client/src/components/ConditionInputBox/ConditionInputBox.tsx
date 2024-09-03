import React, { useContext, useState } from 'react';
import "react-datepicker/src/stylesheets/datepicker.scss";
import { InputProps as IProps, RadioGroup, Stack, Radio } from "@chakra-ui/react";

import { ConditionContext } from 'components/ConditionInput/ConditionInput';


interface ConditionProps extends IProps {
    setCondition?: CallableFunction,
}

const ConditionInputBox: React.FC<ConditionProps> = (props) => {
    const [cond, setCond] = useState('');
    
    const { setCondition } = useContext(ConditionContext)!;

    return (
        <>
        <RadioGroup onChange={(value) => {setCondition(value); setCond(value)}} value={cond}>
        <Stack direction='column'>
            <Radio value='Отличное'>Отличное</Radio>
            <Radio value='Хорошее'>Хорошее</Radio>
            <Radio value='Плохое'>Плохое</Radio>
        </Stack>
        </RadioGroup>
        </>
    );
}

export default ConditionInputBox;