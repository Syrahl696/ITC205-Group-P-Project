package bcccp.tickets.season;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

/**
 * Class SeasonTicket
 */
public class SeasonTicket implements ISeasonTicket {
	
	private List<IUsageRecord> usages;
	private IUsageRecord currentUsage = null;
	
	private String ticketId;
	private String carparkId;
	private long startValidPeriod;
	private long endValidPeriod;
	
        /**
         * Instantiates new Season Ticket
         * @param ticketId
         * @param carparkId
         * @param startValidPeriod
         * @param endValidPeriod 
         */
	public SeasonTicket (String ticketId, 
			             String carparkId, 
			             long startValidPeriod,
			             long endValidPeriod) {
		//TDO Implement constructor
                this.ticketId = ticketId;
		this.carparkId = carparkId;
		this.startValidPeriod = startValidPeriod;
		this.endValidPeriod = endValidPeriod;
	}
/**
 * @see bcccp.tickets.season.ISeasonTicket#getId() 
 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return ticketId;
	}
/**
 * @see bcccp.tickets.season.ISeasonTicket#getCarparkId() 
 */
	@Override
	public String getCarparkId() {
		// TODO Auto-generated method stub
		return carparkId;
	}
/**
 * @see bcccp.tickets.season.ISeasonTicket#getStartValidPeriod() 
 */
	@Override
	public long getStartValidPeriod() {
		// TODO Auto-generated method stub
		return startValidPeriod;
	}
/**
 * @see bcccp.tickets.season.ISeasonTicket#getEndValidPeriod() 
 */
	@Override
	public long getEndValidPeriod() {
		// TODO Auto-generated method stub
		return endValidPeriod;
	}

	@Override
	public boolean inUse() {
		// TODO Auto-generated method stub
                return inUse();
		
	}

	@Override
	public void recordUsage(IUsageRecord record) {
		// TODO Auto-generated method stub
                record = currentUsage;
		
	}

	@Override
	public IUsageRecord getCurrentUsageRecord() {
		// TODO Auto-generated method stub
		return currentUsage;
	}

	@Override
	public void endUsage(long dateTime) {
		// TODO Auto-generated method stub
		 
	}

	@Override
	public List<IUsageRecord> getUsageRecords() {
		// TODO Auto-generated method stub
		return null;
	}


}
