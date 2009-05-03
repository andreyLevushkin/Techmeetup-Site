Tapestry.FieldEventManager.addMethods({
        initialize : function(field)
    {
        this.field = $(field);

        var id = this.field.id;
        this.label = $(id + ':label');
        this.icon = $(id + ':icon');

        document.observe(Tapestry.FOCUS_CHANGE_EVENT, function(event)
        {
            // If changing focus *within the same form* then
            // perform validation.  Note that Tapestry.currentFocusField does not change
            // until after the FOCUS_CHANGE_EVENT notification.

            if (Tapestry.currentFocusField == this.field &&
                this.field.form == event.memo.form)
                ;//this.validateInput();  // NB: disable validation on field focus change

        }.bindAsEventListener(this));
    }
});

Tapestry.ErrorPopup.addMethods({
    initialize : function(field)
    {
        this.field = $(field);

        this.innerSpan = new Element("span");
        this.outerDiv = $(new Element("div", {
            'id' : this.field.id + ":errorpopup",
            'class' : 't-error-popup' })).update(this.innerSpan).hide();

        var body = $$('BODY').first();

        body.insert({ bottom: this.outerDiv });

        this.outerDiv.absolutize();

        this.outerDiv.observe("click", function(event)
        {
            this.ignoreNextFocus = true;

            this.stopAnimation();

            this.outerDiv.hide();

            this.field.activate();

            Event.stop(event);  // Should be domevent.stop(), but that fails under IE
        }.bindAsEventListener(this));

        this.queue = { position: 'end', scope: this.field.id };

        Event.observe(window, "resize", this.repositionBubble.bind(this));

        document.observe(Tapestry.FOCUS_CHANGE_EVENT, function(event)
        {
            if (this.ignoreNextFocus)
            {
                this.ignoreNextFocus = false;
                return;
            }

            if (event.memo == this.field)
            {
                //this.fadeIn();  // NB: prevent existing validations reappearing on field focus change
                return;
            }

            // If this field is not the focus field after a focus change, then it's bubble,
            // if visible, should fade out. This covers tabbing from one form to another.
            this.fadeOut();

        }.bind(this));
    }
}); 