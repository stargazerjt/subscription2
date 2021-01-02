package com.stargate.subscript.services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stargate.subscript.converter.SubscriptionConverter;
import com.stargate.subscript.models.Invoice;
import com.stargate.subscript.models.SubscriptInvModel;
import com.stargate.subscript.models.Subscription;
import com.stargate.subscript.models.SubscriptionRequest;
import com.stargate.subscript.models.SubscriptionResponse;
import com.stargate.subscript.repo.SubscriptionRepo;

@Service
public class SubscriptionService {

	@Autowired
	SubscriptionConverter subConverter;
	@Autowired
	SubscriptionRepo subRepo;

	SimpleDateFormat sdfYr = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfMon = new SimpleDateFormat("MM");
	SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public SubscriptionResponse save(final SubscriptionRequest request)  {
		SubscriptInvModel model = getValidatedRequestToModel(request);
		model=processInvDate(model);
		storeDB(model);

		return subConverter.modelToResponse(model);
	}
	
	private void storeDB(SubscriptInvModel model) {
		try {
			Subscription sub=new Subscription();
			List<Invoice> invLs=new ArrayList<Invoice>();
			
			sub.setDayOfWeekMonthly(model.getDayOfWeekMonthly());
			sub.setSubscriptionType(model.getSubscriptionType());
			sub.setSubscriptAmt(model.getInvAmt());
			sub.setStartDate(model.getStartDate());
			sub.setEndDate(model.getEndDate());
			sub.setCreatedDate(new Date());
			sub.setLastUpdated(new Date());
			
			Iterator<String> invDate=model.getInvDates().iterator();
			
			while(invDate.hasNext()) {
				Invoice inv=new Invoice();
				
				inv.setInvDate(sdf.parse(invDate.next()));
				inv.setCreatedDate(new Date());
				inv.setLastUpdated(new Date());
				inv.setSubscript(sub);
				invLs.add(inv);
			}sub.setInvLs(invLs);
			
			subRepo.save(sub);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private SubscriptInvModel getValidatedRequestToModel(final SubscriptionRequest request){
		
			Period diff = compareDate(request.getStartDate(), request.getEndDate());

			if (diff.getYears() > 0 || (diff.getMonths() == 3 && diff.getDays() > 0) || diff.getMonths() > 4) {
				String msg = "Validation Fail: Cannot more than 3 Months.";
				throw new BadRequestException(Response.status(Status.BAD_REQUEST).entity(msg).build());
			}

			if (request.getSubscriptionType().equals("DAILY")) {
				request.setDayOfWeekMonthly("0");
			} else if (request.getSubscriptionType().equals("WEEKLY")) {

				if (subConverter.convertInt(request.getDayOfWeekMonthly()) == 0) {
					String msg = "Validation Fail: Subscription Weekly on Day of Week have invalid value.";
					throw new BadRequestException(Response.status(Status.BAD_REQUEST).entity(msg).build());
				}
			} else if (request.getSubscriptionType().equals("MONTHLY")) {
				try {
					if (subConverter.convertInt(request.getDayOfWeekMonthly()) > 31) {
						String msg = "Validation Fail: Subscription Weekly on Day of Month have invalid value.";
						throw new BadRequestException(Response.status(Status.BAD_REQUEST).entity(msg).build());
					}
				} catch (NumberFormatException e) {
					String msg = "Validation Fail: Subscription Weekly on Day of Month have invalid value.";
					throw new BadRequestException(Response.status(Status.BAD_REQUEST).entity(msg).build());
				}
			} else {
				String msg = "Validation Fail: Subscription Type have invalid value.";
				throw new BadRequestException(Response.status(Status.BAD_REQUEST).entity(msg).build());
			}
			return subConverter.requestToModel(request);
		
	}
	
	private SubscriptInvModel processInvDate(SubscriptInvModel model) {

		Period diff = compareDate(model.getStartDate(), model.getEndDate());
		List<String> invDates = new ArrayList<String>();

		LocalDate startInvDate = LocalDate.of(Integer.parseInt(sdfYr.format(model.getStartDate())),
				Integer.parseInt(sdfMon.format(model.getStartDate())),
				Integer.parseInt(sdfDay.format(model.getStartDate())));
		LocalDate dueDate = LocalDate.of(Integer.parseInt(sdfYr.format(model.getEndDate())),
				Integer.parseInt(sdfMon.format(model.getEndDate())),
				Integer.parseInt(sdfDay.format(model.getEndDate())));

		if (model.getSubscriptionType().equals("WEEKLY")) {

			while (startInvDate.isBefore(dueDate.plusDays(1))) {
				if (startInvDate.getDayOfWeek().getValue() == model.getDayOfWeekMonthly()) {
					invDates.add(startInvDate.format(formatters));
				}
				startInvDate = startInvDate.plusDays(1);
			}
		} else if (model.getSubscriptionType().equals("MONTHLY")) {
			startInvDate = calcInvDt(model.getStartDate(), model.getDayOfWeekMonthly());

			for (int i = 0; i <= diff.getMonths(); i++) {
				if (startInvDate.isBefore(dueDate.plusDays(1))) {
					invDates.add(startInvDate.format(formatters));
				}
				startInvDate = startInvDate.plusMonths(1);
			}
		} else if (model.getSubscriptionType().equals("DAILY")) {
			while (startInvDate.isBefore(dueDate.plusDays(1))) {
				invDates.add(startInvDate.format(formatters));
				startInvDate = startInvDate.plusDays(1);
			}
		}
		model.setInvDates(invDates);
		
		return model;
	}

	private Period compareDate(Date startDate, Date endDate) {

		LocalDate startDt = LocalDate.of(Integer.parseInt(sdfYr.format(startDate)),
				Integer.parseInt(sdfMon.format(startDate)), Integer.parseInt(sdfDay.format(startDate)));
		LocalDate endDt = LocalDate.of(Integer.parseInt(sdfYr.format(endDate)),
				Integer.parseInt(sdfMon.format(endDate)), Integer.parseInt(sdfDay.format(endDate)));

		return Period.between(startDt, endDt);
	}

	private LocalDate calcInvDt(Date startDate, int monOfDay) {
		try {
			LocalDate startDt = LocalDate.of(Integer.parseInt(sdfYr.format(startDate)),
					Integer.parseInt(sdfMon.format(startDate)), Integer.parseInt(sdfDay.format(startDate)));

			LocalDate invDt = LocalDate.of(Integer.parseInt(sdfYr.format(startDate)),
					Integer.parseInt(sdfMon.format(startDate)), monOfDay);

			if (startDt.compareTo(invDt) < 0) {
				return invDt;
			} else {
				return invDt.plusMonths(1);
			}
		} catch (Exception e) {
			String msg = "Calculation Invoice Date Fail: Day of Month have invalid value.";
			throw new BadRequestException(Response.status(Status.BAD_REQUEST).entity(msg).build());
		}
	}
}
