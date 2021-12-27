with Ada.Text_IO, Ada.Long_Integer_Text_IO;
use Ada.Text_IO, Ada.Long_Integer_Text_IO;

procedure main is
    arr_size : constant Integer := 10;
    my_arr   : array (1 .. arr_size) of Long_Integer;
    result   : Long_Integer;
    -- total : Long_Integer := 0;

    -- my_pack.ads
    procedure init_arr;
    function part
        (left_ind : in Integer; right_ind : in Integer) return Long_Integer;

    protected monitor is
        procedure set_count_tasks(count : Integer);
        procedure set_result(sum : in Long_Integer);
        entry get_total(total_1 : out Long_Integer);
    private
        total : Long_Integer := 0;
        count_tasks : Integer;
    end monitor;

    task type my_task is
        entry start (left1, right1 : in Integer);
        entry finish (result_1 : out Long_Integer);
    end my_task;



    -- my_pack.adb
    procedure init_arr is
    begin
        for i in my_arr'First .. my_arr'Last loop
            my_arr (i) := Long_Integer (i);
        end loop;
    end init_arr;

    function part
        (left_ind : in Integer; right_ind : in Integer) return Long_Integer
    is
        result : Long_Integer := 0;
    begin
        for i in left_ind .. right_ind loop
            result := result + my_arr (i);
        end loop;
        return result;
    end part;
   
    protected body monitor is
        procedure set_count_tasks(count : Integer) is
        begin
            count_tasks := count;
        end set_count_tasks;

        procedure set_result(sum : in Long_Integer) is
        begin
            total := total + sum;
            count_tasks := count_tasks - 1;
        end set_result;

        entry get_total(total_1 : out Long_Integer) when count_tasks = 0 is
        begin
            total_1 := total;
        end get_total;
    end monitor;

    task body my_task is
        left, right : Integer;
        result        : Long_Integer;
    begin
        accept start (left1, right1 : in Integer) do
            left  := left1;
            right := right1;
        end start;

        result := part (left, right);
        monitor.set_result(result);

        -- accept finish (result_1 : out Long_Integer) do
            -- result_1 := result;
        -- end finish;
    end my_task;

    total_1 : Long_Integer;
    var_task : array (1 .. 4) of my_task;
begin
    init_arr;
    monitor.set_count_tasks(var_task'Length);

    for i in var_task'Range loop
        var_task (i).start (1, 5 + i);
    end loop;

    monitor.get_total(total_1);
    Put_Line (total_1'Img);
end main;
