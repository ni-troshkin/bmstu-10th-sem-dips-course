export interface StatMessage {
    eventUuid:  number,
    username:   string,
    action:     string,
    eventStart: Date,
    eventEnd:   Date,
    params:     JSON,
    service:    string
}