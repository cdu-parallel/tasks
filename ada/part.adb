with Ada.Text_IO, Ada.Long_Integer_Text_IO;
use Ada.Text_IO, Ada.Long_Integer_Text_IO;

procedure main is
    arr_size : constant Integer := 10;
    my_arr   : array (1 .. arr_size) of Long_Integer;
    result   : Long_Integer;
    total : Long_Integer := 0;

    procedure init_arr;
    function part
      (left_ind : in Integer; right_ind : in Integer) return Long_Integer;

    task type my_task is
        entry start (left1, right1 : in Integer);
        entry finish (result_1 : out Long_Integer);
    end my_task;

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

    task body my_task is
        left, right : Integer;
        result          : Long_Integer;
    begin
        accept start (left1, right1 : in Integer) do
            left  := left1;
            right := right1;
        end start;

        result := part (left, right);
        accept finish (result_1 : out Long_Integer) do
            result_1 := result;
        end finish;
    end my_task;

    var_task : array (1 .. 4) of my_task;
begin
    init_arr;
    for i in var_task'Range loop
        var_task (i).start (1, 5 + i);
    end loop;

    New_Line;
    for i in var_task'Range loop
        var_task (i).finish (result);
        total := total + result;
        Put_Line (result'Img);
    end loop;

    New_Line;
    Put_Line (total'Img);
end main;
