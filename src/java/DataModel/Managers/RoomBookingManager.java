/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel.Managers;

import DataModel.Model;
import DataModel.RoomBooking;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author x3041557
 */
public class RoomBookingManager extends AbstractManager {
    public RoomBookingManager(Model model) {
        super(model);
    }
    
    //<editor-fold defaultstate="collapsed" desc="ROOMBOOKING">
    private static RoomBooking mapToRoomBooking(Map<String, Object> map) {
        RoomBooking rb = new RoomBooking();
        rb.setCheckin(LocalDate.parse(map.get("checkin").toString()));
        rb.setCheckout(LocalDate.parse(map.get("checkout").toString()));
        rb.setRef((Integer)map.get("b_ref"));
        rb.setRoomNo((Integer)map.get("r_no"));
        return rb;
    }
    
    public List<RoomBooking> getAllRoomBookings() {
        String sql = "SELECT * FROM roombooking";
        Object[] args = {};
        return (List<RoomBooking>)(List<?>)getList(
                sql, 
                args, 
                "getRoomBookingAll", 
                RoomBookingManager::mapToRoomBooking
        );
    }
    
    public List<RoomBooking> getRoomBookings(LocalDate start, LocalDate end) {
        //gets all room bookings which cover any of this period
        String sql = "SELECT * FROM roombooking WHERE " +
                "(checkin <= ? AND checkout > ?) OR (checkin BETWEEN ? AND ?)";
        Object[] args = {
            Date.valueOf(start), 
            Date.valueOf(start), 
            Date.valueOf(start), 
            Date.valueOf(end)
        };
        return (List<RoomBooking>)(List<?>)getList(
                sql, 
                args, 
                "getRoomBookingsDateRange", 
                RoomBookingManager::mapToRoomBooking
        );
    }
    
    public List<RoomBooking> getRoomBookings(int bookingRef) {
        String sql = "SELECT * FROM roombooking WHERE b_ref = ?";
        Object[] args = {bookingRef};
        return (List<RoomBooking>)(List<?>)getList(
                sql, 
                args, 
                "getRoomBookingsBookingRef", 
                RoomBookingManager::mapToRoomBooking
        );
    }
    
    public int createRoomBooking(RoomBooking rb) {
        String sql = "INSERT INTO hotelbooking.roombooking(" +
            "r_no, b_ref, checkin, checkout)" + 
            "VALUES (?, ?, ?, ?)";
        Object[] args = {
            rb.getRoomNo(),
            rb.getRef(),
            rb.getCheckin(),
            rb.getCheckout()
        };
        return createRecord(sql, args, "createRoomBooking");
    }
    //</editor-fold>
}
