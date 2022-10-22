package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingDto> {

    @Override
    public void initialize(StartBeforeEnd constraint) {
    }

    @Override
    public boolean isValid(BookingDto bookingDto, ConstraintValidatorContext context) {

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BadRequestException("end before start");
        }
        return true;
    }
}
