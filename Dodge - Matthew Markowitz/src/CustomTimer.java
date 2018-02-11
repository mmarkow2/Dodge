

class CustomTimer
{
	//time that the timer started at
	//it is a long because that is what System.nanoTime() returns
	long starttime;
	
	//makes initial time equal to 0
	public CustomTimer()
	{
		starttime = 0;
	}
	
	//this gets the system's current time
	public void startTimer()
	{
		starttime = System.nanoTime();
	}
	
	//stops the timer
	public void stopTimer()
	{
		starttime = 0;
	}
	
	//returns whether the timer was started or not
	public boolean hasStarted()
	{
		//if the starttime is still equal to 0, than the timer was not started
		if (starttime > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//returns time in milliseconds
	public int getTime()
	{
		//if the timer was started, then return the time, otherwise return 0
		if (starttime > 0)
		{
			return (int) ((System.nanoTime() - starttime) / 1000000.);
		}
		else
		{
			return 0;
		}
	}
}