package az.developia;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.TemporalUnit;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
//        System.out.println(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()));

//        testDaysAndMonths();
//        testLocalDate();
//        testYearMonth();
//        testMonthDay();
//        testYear();
//        testLocalTime();
//        testLocalDateTime();
//        testZoneId();
//        testZonedDateTime();
//        testOffsetTime();
//        testInstant();
//        testDateTimeFormatting();
        testTemporalAdjusters();
    }

    public static void testDaysAndMonths() {
        var dayOfWeek = DayOfWeek.TUESDAY;
        var displayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("az"));
        System.out.println(displayName);

        var month = Month.JANUARY;
        System.out.println(month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("az")));
        var daysInMonth = month.maxLength();
        System.out.println(daysInMonth);
    }

    public static void testLocalDate() {
        var todaysDate = LocalDate.now();
        System.out.println("Today: " + todaysDate);
        var wednesdayAfterTodaysDate = todaysDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        System.out.println("First Wednesday date is: " + wednesdayAfterTodaysDate);

        var plusToDate = todaysDate.plus(Period.ofDays(2));
        System.out.println(plusToDate);

        var dateFrom = LocalDate.of(2019, 2, 2);
        System.out.println(dateFrom.getDayOfWeek());
    }

    public static void testYearMonth() {
        var year = YearMonth.now();
        System.out.println(year);

        System.out.println(year.getMonthValue());
        System.out.println(year.getYear());
        System.out.println(year.getMonth());
    }

    public static void testMonthDay() {
        var monthDay = MonthDay.now();
        System.out.println(monthDay.getDayOfMonth());
        System.out.println(monthDay.getMonth());
        System.out.println(monthDay.with(Month.SEPTEMBER));

        var date = MonthDay.of(Month.FEBRUARY, 29);
        boolean validLeapYear = date.isValidYear(2010);
        System.out.println(validLeapYear);
    }

    public static void testYear() {
        var year = Year.now();
        System.out.println(year);
        var combineToMonth = year.atMonth(Month.SEPTEMBER);
        System.out.println(combineToMonth);
        var combineToLocalDate = combineToMonth.atDay(new Random().nextInt(Month.SEPTEMBER.maxLength()));
        System.out.println(combineToLocalDate);
    }

    public static void testLocalTime() {
        var timeNow = LocalTime.now();
        System.out.println(timeNow);

        // calculate between times
        System.out.println(timeNow.until(LocalTime.of(21, 0), ChronoUnit.HOURS));

        var combinedWithDate = timeNow.atDate(LocalDate.of(2020, 1, 2));
        System.out.println(combinedWithDate);
    }

    public static void testLocalDateTime() {
        var timeNow = LocalDateTime.now();
        System.out.println(timeNow.getDayOfMonth());
        System.out.println(timeNow.getDayOfWeek());
    }

    public static void testZoneId() {
        var availableZones = ZoneId.getAvailableZoneIds();
        var timeNow = LocalDateTime.now();

        System.out.println("|  Zone Id  |  Zone Offset  |  Time  |");
        availableZones.stream()
                .sorted()
                .forEach(zoneIdStr -> {
                    var zoneId = ZoneId.of(zoneIdStr);
                    var zonedTime = timeNow.atZone(zoneId);
                    var zoneOffset = zonedTime.getOffset();
                    System.out.println(zoneId + " | " + zoneOffset + " | " + timeNow);
                });

    }

    public static void testZonedDateTime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");

        LocalDateTime leaving = LocalDateTime.now();
        ZoneId leavingZone = ZoneId.of("Asia/Baku");
        ZonedDateTime departure = ZonedDateTime.of(leaving, leavingZone);

        try {
            String out1 = departure.format(format);
            System.out.printf("LEAVING:  %s (%s)%n", out1, leavingZone);
        } catch (DateTimeException exc) {
            System.out.printf("%s can't be formatted!%n", departure);
            throw exc;
        }

        ZoneId arrivingZone = ZoneId.of("America/Los_Angeles");
        ZonedDateTime arrival = departure
                .withZoneSameInstant(arrivingZone)
                .plusMinutes(650);

        try {
            String out2 = arrival.format(format);
            System.out.printf("ARRIVING: %s (%s)%n", out2, arrivingZone);
        } catch (DateTimeException exc) {
            System.out.printf("%s can't be formatted!%n", arrival);
            throw exc;
        }

        if (arrivingZone.getRules().isDaylightSavings(arrival.toInstant()))
            System.out.printf("  (%s daylight saving time will be in effect.)%n",
                    arrivingZone);
        else
            System.out.printf("  (%s standard time will be in effect.)%n",
                    arrivingZone);
    }

    public static void testOffsetTime() {
        LocalDateTime localDate = LocalDateTime.now();
        ZoneOffset offset = ZoneOffset.of("+04:00");

        OffsetDateTime offsetDate = OffsetDateTime.of(localDate, offset);
        OffsetDateTime lastThursday =
                offsetDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.THURSDAY));
        System.out.printf("The last Thursday is the %sth.%n",
                lastThursday.getDayOfMonth());
    }

    public static void testInstant() {
        var instantTime = Instant.now();
        System.out.println(instantTime.toString());
        var hour = instantTime.get(ChronoField.NANO_OF_SECOND);
        System.out.println(hour);

        var oneHourLater = instantTime.plus(1, ChronoUnit.HOURS);
        System.out.println(oneHourLater);

        var secondsFromEpoch = Instant.ofEpochSecond(0L).until(Instant.now(),
                ChronoUnit.SECONDS);

        System.out.println(secondsFromEpoch);
    }

    public static void testDateTimeFormatting() {
        // parse
        var dateInString = "Jan 2 2021";
        var parsedDate = LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("MMM d yyyy"));
        System.out.println(parsedDate);

        // format
        var timeNow = LocalDateTime.now();
        var formattedTime = timeNow.format(DateTimeFormatter.ISO_DATE);
        System.out.println(formattedTime);
    }

    public static void testTemporalAdjusters() {
        var date = LocalDate.now();
        DayOfWeek dotw = date.getDayOfWeek();
        System.out.printf("%s is on a %s%n", date, dotw);

        System.out.printf("first day of Month: %s%n",
                date.with(TemporalAdjusters.firstDayOfMonth()));
        System.out.printf("first Monday of Month: %s%n",
                date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));
        System.out.printf("last day of Month: %s%n",
                date.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.printf("first day of next Month: %s%n",
                date.with(TemporalAdjusters.firstDayOfNextMonth()));
        System.out.printf("first day of next Year: %s%n",
                date.with(TemporalAdjusters.firstDayOfNextYear()));
        System.out.printf("first day of Year: %s%n",
                date.with(TemporalAdjusters.firstDayOfYear()));

    }

    public static void testTemporalQuery() {
        TemporalQuery<TemporalUnit> query = TemporalQueries.precision();
        System.out.printf("LocalDate precision is %s%n",
                LocalDate.now().query(query));
        System.out.printf("LocalDateTime precision is %s%n",
                LocalDateTime.now().query(query));
        System.out.printf("Year precision is %s%n",
                Year.now().query(query));
        System.out.printf("YearMonth precision is %s%n",
                YearMonth.now().query(query));
        System.out.printf("Instant precision is %s%n",
                Instant.now().query(query));
    }

    public static void testDuration() {

    }
}





