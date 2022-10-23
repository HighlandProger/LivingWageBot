//package ru.rusguardian.domain;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.util.stream.Stream;
//
//@Converter(autoApply = true)
//public class StatusConverter implements AttributeConverter<Status, String> {
//
//    @Override
//    public String convertToDatabaseColumn(Status Status) {
//        if (Status == null) {
//            return null;
//        }
//        return Status.name();
//    }
//
//    @Override
//    public Status convertToEntityAttribute(String status) {
//        if (status == null) {
//            return null;
//        }
//
//        return Stream.of(Status.values())
//                .filter(c -> c.name().equals(status))
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//    }
//}
