/**
 *  Holiday Color Lights
 *
 *  Copyright 2016 ygelfand
 *
 * Modified by cleight, 2020. Added additional configuration boxes for holidy dates and bulbe colors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Holiday Color Lights",
    namespace: "ygelfand",
    author: "ygelfand",
    description: "This SmartApp will change the color of selected lights based on closest holiday colors",
    category: "Convenience",
    iconUrl: "https://lh5.ggpht.com/xJkWtYJeUMcCz2oCc3XbN1c5xbNY87RXj3FD3yUx0k1eQ71e16WJPlq6b404Sk1qIw=w60",
    iconX2Url: "https://lh5.ggpht.com/xJkWtYJeUMcCz2oCc3XbN1c5xbNY87RXj3FD3yUx0k1eQ71e16WJPlq6b404Sk1qIw=w120",
    iconX3Url: "https://lh5.ggpht.com/xJkWtYJeUMcCz2oCc3XbN1c5xbNY87RXj3FD3yUx0k1eQ71e16WJPlq6b404Sk1qIw=w180")


preferences {
    page(name: "configurationPage")
    }
    
def configurationPage() {
	dynamicPage(name: "configurationPage", title: "Holidays setup",uninstall: true, install: true) {
		section("Lights Schedule") {
        	input "globalEnable", "bool", title: "Enabled?", defaultValue: true, required: true
            input "startTimeType", "enum", title: "Starting at", options: [["time": "A specific time"], ["sunrise": "Sunrise"], ["sunset": "Sunset"]], defaultValue: "time", submitOnChange: true
            if (startTimeType in ["sunrise","sunset"]) {
                input "startTimeOffset", "number", title: "Offset in minutes (+/-)", range: "*..*", required: false
            }
            else {
                input "starting", "time", title: "Start time", required: false
            }
            input "endTimeType", "enum", title: "Ending at", options: [["time": "A specific time"], ["sunrise": "Sunrise"], ["sunset": "Sunset"]], defaultValue: "time", submitOnChange: true
            if (endTimeType in ["sunrise","sunset"]) {
                input "endTimeOffset", "number", title: "Offset in minutes (+/-)", range: "*..*", required: false
            }
            else {
                input "ending", "time", title: "End time", required: false
            }
      		input "days", "enum", title: "Day of the week", required: true, multiple: true, options: [
            	"Sunday",
				"Monday",
				"Tuesday",
				"Wednesday",
				"Thursday",
				"Friday",
				"Saturday"
            	], defaultValue:  [
            	"Sunday",
				"Monday",
				"Tuesday",
				"Wednesday",
				"Thursday",
				"Friday",
				"Saturday" ] 
 		}
        section("Only When") {
        	input "toggleSwitch", "capability.switch", title: "Only run when this switch is on", required: false, multiple: false
            input "modes", "mode", title: "Only when mode is", required: false, multiple: true
        }
		section("Light Settings") {
            input "lights", "capability.colorControl", title: "Which Color Changing Bulbs?", multiple:true, required: true
        	input "brightnessLevel", "number", title: "Brightness Level (1-100)?", required:false, defaultValue:100, range: '1..100'
		}
		section("Holidays Settings") {
			input "holidays", "enum", title: "Holidays?", required: true, multiple: true, options: holidayNames(), defaultValue: holidayNames()
        	input "maxdays", "number", title: "Maximum number of days around a holiday? (-1 for unlimited)", range: '-1..60', required: true, defaultValue: -1
        	input "forceholiday", "enum", title: "Force a specific holiday?", required: false, multiple: false, options: holidayNames()
		}
		section("Frequency") {
            input "cycletime", "enum", title: "Cycle frequency?" , options: [
				[1:"1 minute"],
                [5:"5 minutes"],
				[10:"10 minutes"],
				[15:"15 minutes"],
				[30:"30 minutes"],
				[60:"1 hour"],
				[180:"3 hours"],
			], required: true, defaultValue: "10", multiple: false
			input "seperate", "enum", title: "Cycle each light individually, or all together?", required: true, multiple: false, defaultValue: "individual", options: [
				[individual:"Individual"],
				[combined:"Combined"],
			]
    		input "holidayalgo", "enum", title: "Color selection", required: true, multiple: false, defaultValue: "closest", submitOnChange: true, options: [
    			[closest:"Closest Holiday"],
            	[closestwgo:"Next Holiday (with linger)"]
    		]
        	if(holidayalgo == "closestwgo") {
            	input "lingerdays", "number", title: "Days to linger after the holiday", required: true, defaultValue: 0
        	}
		}
		section ("Holiday Dates (Enter in MM/DD) and Colors") {
			input "NYD", "date", title: "New Years Day:", required: true, defaultValue: "2020-01-01"
			input "NYD_C", "enum", title: "New Years Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["White", "Red", "Pink", "Purple"]
			input "LEO", "date", title: "Law Enforcement Day:", required: true, defaultValue: "2020-01-09"
            input "LEO_C", "enum", title: "Law Enforcement Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Blue", "Navy Blue"]
			input "VD", "date", title: "Valentines Day:", required: true, defaultValue: "2020-02-14"
            input "VD_C", "enum", title: "Valentines Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "Pink", "Rasberry", "Purple", "Indigo"]
			input "PD", "date", title: "Presidents Day:", required: true, defaultValue: "2020-02-17"
            input "PD_C", "enum", title: "Presidents Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "White", "Blue"]
			input "SPD", "date", title: "St. Patrick's Day:", required: true, defaultValue: "2020-03-17"
            input "SPD_C", "enum", title: "St. Patrick's Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Green", "Orange"]
			input "ED", "date", title: "Easter:", required: true, defaultValue: "2020-04-12"
            input "ED_C", "enum", title: "Easter Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Pink", "Turquoise", "Aqua"]
			input "MD", "date", title: "Mothers Day:", required: true, defaultValue: "2020-05-10"
            input "MD_C", "enum", title: "Mothers Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "Pink"]
			input "MEM", "date", title: "Memorial Day:", required: true, defaultValue: "2020-05-25"
            input "MEM_C", "enum", title: "Memorial Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "White", "Blue"]
			input "FD", "date", title: "Fathers Day:", required: true, defaultValue: "2020-06-21"
            input "FD_C", "enum", title: "Fathers Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Blue", "Navy Blue"]
			input "IND", "date", title: "Indenpendence Day:", required: true, defaultValue: "2020-07-04"
            input "IND_C", "enum", title: "Indenpendence Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "White", "Blue"]
			input "LD", "date", title: "Labor Day:", required: true, defaultValue: "2020-09-07"
            input "LD_C", "enum", title: "Labor Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "White", "Blue"]
			input "HALL", "date", title: "Halloween:", required: true, defaultValue: "2020-10-31"
            input "HALL_C", "enum", title: "Halloween Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Orange", "Safety Orange"]
			input "VET", "date", title: "Veterans Day:", required: true, defaultValue: "2020-11-11"
            input "VET_C", "enum", title: "Veterans Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "White", "Blue"]
			input "TG", "date", title: "Thanksgiving Day:", required: true, defaultValue: "2020-11-26"
            input "TG_C", "enum", title: "Thanksgiving Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Orange", "Safety Orange"]
			input "CD", "date", title: "Christmas Day:", required: true, defaultValue: "2020-12-25"
            input "CD_C", "enum", title: "Christmas Day Colors:", required: true, multiple: true, options: colorChoice(), defaultValue: ["Red", "Green"]
        }
	}
}
def colorChoice () {
    return [
	"White",
	"Daylight",
	"Soft White",
	"Warm White",
	"Navy Blue",
	"Blue",
	"Green",
	"Turquoise",
	"Aqua",
	"Amber",
	"Yellow",
	"Safety Orange",
	"Orange",
	"Indigo",
	"Purple",
	"Pink",
	"Rasberry",
	"Red",
	"Brick Red"]
}

def allHolidayList() {
    return [
    [name: "New Years Day", day: NYD, colors: NYD_C],
    [name: "Law Enforcement Day", day: LEO, colors: LEO_C ],
    [name: "Valentine's Day", day: VD, colors: VD_C ],
    [name: "Presidents Day", day: PD, colors: PD_C ],
    [name: "St. Patrick's Day", day: SPD, colors: SPD_D ],
    [name: "Easter", day: ED, colors: ED_C ],
    [name: "Mothers Day", day: MD, colors: MD_C ],
    [name: "Memorial Day", day: MEM, colors: MEM_C ],
    [name: "Fathers Day", day: FD, , colors: FD_C ],
    [name: "Independence Day", day: IND, colors: IND_C ],
    [name: "Labor Day", day: LD, colors: LD_C ],
    [name: "Halloween", day: HALL, colors: HALL_C ],
    [name: "Veterans Day", day: VET, colors: VET_C ],
    [name: "Thanksgiving", day: TG, colors: TG_C ],
    [name: "Christmas Day", day: CD, colors: CD_C ]
	]

}

def holidayList() {
	return allHolidayList().findAll {holidays.contains(it.name)}
}

def holidayNames() {
    allHolidayList().name
}

def holidayTimestamps()  {
    def today = new Date()
	def this_year = today.format('Y')
	def last_year = (today - 365 ).format('Y')
	def next_year = (today + 365 ).format('Y')
	def timestamps = [:]
    holidayList().each {
        timestamps[Date.parse("${it.day}/${last_year} 23:59:59")] = it.name
        timestamps[Date.parse("${it.day}/${this_year} 23:59:59")] = it.name
        timestamps[Date.parse("${it.day}/${next_year} 23:59:59")] = it.name
    }
    return timestamps.sort()
}

/* Get Sunrise and Sunset Time */
def riseAndSet = getSunriseAndSunset()
log.debug riseAndSet.sunrise
log.debug riseAndSet.sunset

private timeWindowStart() {
    def result = null
    if (startTimeType == "sunrise") {
        result = riseAndSet.sunrise
        if (result && startTimeOffset) {
            result = new Date(result.time + Math.round(startTimeOffset * 60000))
        }
    }
    else if (startTimeType == "sunset") {
        result = riseAndSet.sunset
        if (result && startTimeOffset) {
            result = new Date(result.time + Math.round(startTimeOffset * 60000))
        }
    }
    else if (starting && location.timeZone) {
        result = timeToday(starting, location.timeZone)
    }
    log.trace "timeWindowStart = ${result}"
    result
}

private getSwitchOk(){
  def  result = !toggleSwitch || (toggleSwitch.currentSwitch == "on")
}
private getModeOk() {
    def result = !modes || modes.contains(location.mode)
        result
}
private getTimeOk() {
    def result = true
    def start = timeWindowStart()
    def stop = timeWindowStop()
    if (start && stop && location.timeZone) {
        result = timeOfDayIsBetween(start, stop, new Date(), location.timeZone)
    }
    log.trace "timeOk = $result"
    result
}
private getDaysOk() {
	def df = new java.text.SimpleDateFormat("EEEE")
	if (location.timeZone) {
		df.setTimeZone(location.timeZone)
	}
	else {
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
	}
	def day = df.format(new Date())
	days.contains(day)
}
private timeWindowStop() {
    def result = null
    if (endTimeType == "sunrise") {
        result = location.currentState("sunriseTime")?.dateValue
        if (result && endTimeOffset) {
            result = new Date(result.time + Math.round(endTimeOffset * 60000))
        }
    }
    else if (endTimeType == "sunset") {
        result = location.currentState("sunsetTime")?.dateValue
        if (result && endTimeOffset) {
            result = new Date(result.time + Math.round(endTimeOffset * 60000))
        }
    }
    else if (ending && location.timeZone) {
        result = timeToday(ending, location.timeZone)
    }
    log.trace "timeWindowStop = ${result}"
    result
}
def closestWithoutGO(buffer=0) {
    def today = new Date()
    today = today - buffer
    def target = today.getTime()
    def last = null
    def diff = null
     holidayTimestamps().any { k, v ->
        if (k > target) {
            last = v
            diff = k
            return true
        }
        return false
    }
    if ((maxdays == -1) || ( diff < ( maxdays  * 86400000) ))
    	return last
    else
    	return null
}
def closest() {
    def today = new Date()
    def last = null
    def distance = 99999999999999
     holidayTimestamps().each { k, v ->
        def d = k - today.getTime()
        if (d.abs() < distance) {
            distance = d.abs()
            last = v
        }
    }
    if ((maxdays == -1) || ( distance < ( maxdays  * 86400000) ))
    	return last
    else
    	return null
}


def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	def hours = settings.cycletime.toInteger().intdiv(60)
    def minutes = settings.cycletime.toInteger() % 60
    def hourmark
    if(hours > 0)
    	hourmark = "${hours}"
    else
    	hourmark = "*"
	schedule("0 0/${minutes} ${hourmark} 1/1 * ? *",changeHandler)
    state.colorOffset=0
}

def changeHandler(evt) {
	if(!globalEnable || !getTimeOk() || !getDaysOk() || !getModeOk() || !getSwitchOk() ) 
		return true
    if (lights)
    {
    	def colors = []
        def curHoliday
        if(forceholiday) {
        	curHoliday = forceholiday
        }
        else 
        {
    		switch(holidayalgo) {
     	   		case 'closest':
                	curHoliday = closest()
            		break;
            	case 'closestwgo':
                	curHoliday = closestWithoutGO(lingerdays)
            		break;
 			}
        }
        log.debug curHoliday
        if (!curHoliday) {
        	log.debug "No holiday around..."
        	return false;
           }
        colors = allHolidayList().find {it.name == curHoliday }.colors
		def onLights = lights.findAll { light -> light.currentSwitch == "on"}
        def numberon = onLights.size();
        def numcolors = colors.size();
        //log.debug "Offset: ${state.colorOffset}"
    	if (onLights.size() > 0) {
        	if (state.colorOffset >= numcolors ) {
            	state.colorOffset = 0
            }
			if (seperate == 'combined')
				sendcolor(onLights,colors[state.colorOffset])
            else {
            	log.debug "Colors: ${colors}"
           		for(def i=0;i<numberon;i++) {
                	sendcolor(onLights[i],colors[(state.colorOffset + i) % numcolors])
                }
            }
            state.colorOffset = state.colorOffset + 1
     	}
   	}
}

def sendcolor(lights,color)
{
log.debug "In send color"
	if (brightnessLevel<1) {
		brightnessLevel=1
	}
    else if (brightnessLevel>100) {
		brightnessLevel=100
	}

	def colorPallet = [
    	"White": [ hue: 0, saturation: 0],
    	"Daylight":  [hue: 53, saturation: 91],
    	"Soft White": [hue: 23, saturation: 56],
    	"Warm White": [hue: 20, saturation: 80],
    	"Navy Blue": [hue: 61, saturation: null],
    	"Blue": [hue: 65, saturation: null ],
    	"Green": [hue: 33, saturation: null ],
    	"Turquoise": [hue: 47, saturation: null ],
    	"Aqua": [hue: 50, saturation: null],
    	"Amber": [hue: 13, saturation: null],
    	"Yellow": [hue: 17, saturation: null],
    	"Safety Orange": [hue: 7, saturation: null],
    	"Orange": [hue: 10, saturation: null],
    	"Indigo": [hue: 73, saturation: null],
    	"Purple": [hue: 82, saturation: 100],
    	"Pink": [hue: 90.78, saturation: 67.84 ],
    	"Rasberry": [hue: 94 , saturation: null ],
    	"Red": [hue: 0, saturation: null ],
    	"Brick Red": [hue: 4, saturation: null ],
	]
	def newcolor = colorPallet."${color}"
    if(newcolor.saturation == null) newcolor.saturation = 100
    newcolor.level = brightnessLevel
	lights*.setColor(newcolor)
    log.debug "Setting Color = ${color} for: ${lights}"

}
